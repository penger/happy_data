from py2neo import Graph, Node, Relationship, NodeMatcher
from time import sleep
from threading import Thread, Lock
import subprocess
import logging

graph = Graph("bolt://192.168.1.123:7687", auth=("neo4j", "happy_cassini"))
# 查询所有的“ProcessInstance”节点
result = graph.run("MATCH (p:ProcessInstance) RETURN p")

# 设置日志记录器
logging.basicConfig(filename='example.log', level=logging.INFO)


class Tasks(object):
    def __init__(self):
        self._tasks = []
        self._lock = Lock()

    def addAll(self, items):
        self._tasks.append(items)

    def get_then_remove(self, task) -> str:
        # 获取锁
        self._lock.acquire()
        try:
            if self._tasks.__contains__(task):
                self._tasks.remove(task)
                return task
            else:
                return ''
        finally:
            self._lock.release()

    def add(self, task):
        # 获取锁
        self._lock.acquire()
        try:
            self._tasks.append(task)
        finally:
            self._lock.release()

    def exists(self, task):
        self._lock.acquire()
        try:
            return self._tasks.__contains__(task)
        finally:
            self._lock.release()

    def is_empty(self) -> bool:
        return len(self._tasks) == 0

    def current_tasks(self):
        return self._tasks


# start 作为起点，每个节点启动一个线程 ，迭代运行

class execute_tasks(Thread):
    def __init__(self, name: str, interval: int, tasks: Tasks):
        super().__init__()
        self.name = name
        self.interval = interval
        self.tasks = tasks

    def run(self):
        # 如果当前任务为结束节点，结束任务，将processInstance 设置为完成
        if self.tasks.get_then_remove(self.name) != '':
            set_task_color(self.name, 'yellow')
            # sleep(self.interval)
            run_log = subprocess.run(
                ['java', '-cp', r'D:\code\github\iron_man\target\iron_man-1.0-SNAPSHOT.jar', 'org.example.SleepTask',
                 str(self.interval)],
                shell=False, stdout=subprocess.PIPE)
            print(run_log)
            sleep(5)
            # 将返回结果写入日志文件
            logging.info(run_log.stdout)
            set_task_color(self.name, 'green')
            print(f"finish task {self.name}")
            # 激活后续节点
            next_nodes = graph.run(
                "MATCH(n:TaskInstance{name:\"" + self.name + "\"})-[:NEXT]->(m:TaskInstance) return m")
            for next_node in next_nodes:
                key = next_node['m']['name']
                # 后续节点 遍历前置节点是否还在
                pre_nodes = graph.run("MATCH(n:TaskInstance{name:\"" + key + "\"})<-[:NEXT]-(m:TaskInstance) RETURN m")
                has_pre = False
                for pre_node in pre_nodes:
                    pre_name = pre_node['m']['name']
                    if self.tasks.exists(pre_name):
                        has_pre = True
                        break
                if not has_pre:
                    execute_tasks(key, self.interval, self.tasks).start()
        else:
            return


def execute_process(process_name: str):
    tasks = Tasks()
    matcher = NodeMatcher(graph)
    process_instance = matcher.match("ProcessInstance", name=process_name).first()
    print(f"当前执行任务的信息为：{process_instance}")
    # 查询所有的 subtask
    all_tasks_cursor = graph.run('MATCH(n:ProcessInstance{date:"2023-04-20"}) -[:SUB_TASK]->(m:TaskInstance) return m')
    for i in all_tasks_cursor:
        tasks.add(i['m']['name'])
    # 启动任务
    execute_tasks("start", 20000, tasks).start()
    while not tasks.is_empty():
        sleep(1)
        # print(f"子任务依然依然有：{tasks.current_tasks()}")
    print("执行完成")


# 更改 传入 taskid 的color
def set_task_color(task_name: str, color: str):
    matcher = NodeMatcher(graph)
    task_instance = matcher.match('TaskInstance', name=task_name).first()
    task_instance.update({'color': color})
    graph.push(task_instance)


if __name__ == "__main__":
    execute_process(process_name="测试的任务")

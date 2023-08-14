import time
import threading
import websockets
from watchdog.observers import Observer
from watchdog.events import FileSystemEventHandler


# 定义文件系统事件处理程序
class MyHandler(FileSystemEventHandler):
    def on_any_event(self, event):
        # 监听到任何文件系统事件时触发
        if event.is_directory:
            return None

        # 构造要发送的消息内容
        message = f"文件 '{event.src_path}' {event.event_type}."

        # 通过 WebSocket 发送消息到前端
        ws.send(message)


# 启动 WebSocket 服务
ws = websockets.WebSocket()
ws.connect("ws://localhost:8000/ws")

# 启动文件系统时间监听器
observer = Observer()
event_handler = MyHandler()
observer.schedule(event_handler, path='/path/to/watched/directory', recursive=True)
observer.start()

# 在主线程中保持 WebSocket 连接
while True:
    time.sleep(1)

# -*- coding: UTF-8 -*-
import subprocess
import logging

# 设置日志记录器
logging.basicConfig(filename='example.log', level=logging.INFO)

# 执行shell命令并获取返回结果
# result = subprocess.run('ls -l', shell=True, stdout=subprocess.PIPE)

run_log = subprocess.run(
                ['java', '-cp', r'D:\code\github\iron_man\target\iron_man-1.0-SNAPSHOT.jar', 'org.example.SleepTask','12300'],
                shell=False, stdout=subprocess.PIPE)
# 将返回结果写入日志文件
logging.info(run_log.stdout)
# logging.info(result.stdout.decode())
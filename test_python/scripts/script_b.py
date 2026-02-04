"""
脚本 b - 提供打印 hello 的方法
"""
import os

def print_hello():
    current_dir = os.getcwd()
    print(f"b 当前运行目录: {current_dir}")
    """打印 hello 消息"""
    print("Hello from script_b!")

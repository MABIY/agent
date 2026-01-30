import os
from datetime import datetime

print("当前目录下的文件：")
print("-" * 50)

for file in os.listdir('.'):
    path = os.path.join('.', file)
    size = os.path.getsize(path)
    mtime = datetime.fromtimestamp(os.path.getmtime(path))
    print(f"{file:25}  {size:6,} 字节   {mtime:%Y-%m-%d %H:%M}")

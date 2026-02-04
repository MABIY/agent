"""
脚本 a - 列出当前目录和递归列出所有目录和文件
"""
import os
from script_b import print_hello


def list_directory_recursive(path='.', indent=0):
    """
    递归列出目录和文件
    
    Args:
        path: 要列出的目录路径
        indent: 缩进级别
    """
    try:
        items = sorted(os.listdir(path))
        for item in items:
            item_path = os.path.join(path, item)
            # 打印当前项，使用缩进显示层级
            print('  ' * indent + '├── ' + item)
            
            # 如果是目录，递归列出
            if os.path.isdir(item_path):
                list_directory_recursive(item_path, indent + 1)
    except PermissionError:
        print('  ' * indent + '  [权限被拒绝]')


def main():
    """主函数"""
    # 获取当前运行目录
    current_dir = os.getcwd()
    print(f"当前运行目录: {current_dir}")
    print("\n目录结构:")
    print(current_dir)
    
    # 递归列出当前目录下的所有目录和文件
    list_directory_recursive(current_dir)
    
    # 调用脚本 b 中的方法
    print("\n调用脚本 b 的方法:")
    print_hello()


if __name__ == "__main__":
    main()

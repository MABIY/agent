# 文件名建议： demo_with_log.py
# 用途：演示带详细执行标识的简单脚本模板

import datetime
import time
import sys


def log(msg):
    """打印带时间戳的日志，方便看出执行顺序和耗时"""
    now = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    print(f"[{now}] {msg}", flush=True)


def main():
    log("脚本开始执行".center(60, "="))
    log("Python 版本: " + sys.version.splitlines()[0])
    log("当前工作目录: " + sys.path[0])

    # ─────────────── 第一步 ───────────────
    log("步骤 1：模拟读取数据...")
    time.sleep(1.2)           # 假装在做事
    data = [10, 25, 7, 42, 18]
    log(f"读取到 {len(data)} 条数据 → {data}")

    # ─────────────── 第二步 ───────────────
    log("步骤 2：开始计算...")
    total = sum(data)
    average = total / len(data) if data else 0
    time.sleep(0.8)
    log(f"总和 = {total}，平均值 = {average:.2f}")

    # ─────────────── 第三步 ───────────────
    log("步骤 3：模拟保存结果...")
    time.sleep(0.6)
    result_text = f"计算完成 - 总和:{total} 平均:{average:.2f}"
    log(f"结果：{result_text}")

    # 你可以在这里加入真正的文件写入
    # with open("result.txt", "w", encoding="utf-8") as f:
    #     f.write(result_text)

    log("所有步骤执行完毕".center(60, "="))
    log("脚本正常结束 ✓")


if __name__ == "__main__":
    try:
        main()
    except KeyboardInterrupt:
        print("\n[用户手动中断]")
    except Exception as e:
        print(f"\n【发生错误】 {type(e).__name__}: {e}")
        import traceback
        traceback.print_exc()
    finally:
        print("\n[程序退出]\n")
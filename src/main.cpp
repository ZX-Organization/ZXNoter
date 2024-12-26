
#include <iostream>
#include <ostream>
#include <qapplication.h>
#include <thread>
#include <fstream>
#include <filesystem>
#include "qt/ui/mainwindow.h"

std::filesystem::path qss_path("../src/qt/ui/resources/cnm.qss");

std::string loadFile(std::filesystem::path *filePath) {
    std::ifstream file(*filePath);
    if (!file.is_open()) {
        std::cerr << "Could not open file: " << filePath << std::endl;
        return "";
    }
    return std::string((std::istreambuf_iterator<char>(file)),
                       std::istreambuf_iterator<char>());
}

// 定时检测文件修改并动态更新内容
void monitorFileAndUpdate(QApplication *app) {
    // 获取文件的初始修改时间
    auto lastModifiedTime = std::filesystem::last_write_time(qss_path);

    while (true) {
        // 每隔指定时间检查一次
        std::this_thread::sleep_for(std::chrono::milliseconds(200));

        // 检测当前文件的修改时间
        auto currentModifiedTime = std::filesystem::last_write_time(qss_path);

        if (currentModifiedTime != lastModifiedTime) {
            lastModifiedTime = currentModifiedTime;

            // 重新加载文件内容
            std::string content = loadFile(&qss_path);
            if (!content.empty()) {
                // 动态更新（这里模拟一个更新行为）
                std::cout << "File updated! New content:\n" << content << std::endl;
                auto qs = QString::fromStdString(content);
                app->setStyleSheet(qs);
            }
        }
    }
}




int main(int argc, char *argv[]) {
    std::cout << "nmsl " << std::endl;
    QApplication app(argc, argv);

    // 测试main窗口
    MainWindow w;
    w.show();

    std::thread t(monitorFileAndUpdate, &app);
    t.detach();

    // 进入qt事件循环
    return app.exec();
}
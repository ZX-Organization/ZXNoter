
#include <iostream>
#include <ostream>
#include <qapplication.h>
#include <thread>
#include <fstream>
#include <filesystem>
#include "qt/concurrent_preview.h"
#include "qt/ui/mainwindow.h"

std::filesystem::path qss_path("../src/qt/ui/resources/cnm.qss");

ConcurrentPreview *cp;


int main(int argc, char *argv[]) {
    std::cout << "nmsl " << std::endl;
    QApplication app(argc, argv);

    // 测试main窗口
    MainWindow w;
    w.show();
    cp = new ConcurrentPreview(&app, &qss_path);
    cp->Start();

    // 进入qt事件循环
    return app.exec();
}
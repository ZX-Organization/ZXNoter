
#include <qapplication.h>
#include <qsurfaceformat.h>

#include <filesystem>
#include <iostream>
#include <ostream>

#include "qt/concurrent_preview.h"
#include "qt/ui/mainwindow.h"

std::filesystem::path qss_path("../src/qt/ui/resources/cnm.qss");

ConcurrentPreview *cp;

int main(int argc, char *argv[]) {
    std::cout << "nmsl " << std::endl;
    QApplication app(argc, argv);
    // 请求opengl版本
    QSurfaceFormat format;
    format.setVersion(4, 1);
    format.setProfile(QSurfaceFormat::CoreProfile);
    QSurfaceFormat::setDefaultFormat(format);

    // 测试main窗口
    MainWindow w;
    w.show();
    cp = new ConcurrentPreview(&app, &qss_path);
    cp->Start();

    // 进入qt事件循环
    return app.exec();
}

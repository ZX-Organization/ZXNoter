#include <iostream>
#include <qapplication.h>
#include "qt/ui/mainwidget.h"

int main(int argc , char* argv[]){
    std::cout << "nmsl " << std::endl;
    QApplication app(argc,argv);

    // 测试main窗口
    mainwidget w;
    w.show();

    // 进入qt事件循环
    return app.exec();
}
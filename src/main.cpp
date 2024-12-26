#include "qt/ui/mainwindow.h"

#include <iostream>
#include <ostream>
#include <qapplication.h>

int main(int argc, char *argv[]) {
  std::cout << "nmsl " << std::endl;
  QApplication app(argc, argv);

  // 测试main窗口
  MainWindow w;
  w.show();

  // 进入qt事件循环
  return app.exec();
}
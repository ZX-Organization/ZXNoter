#include "qt/ui/mainwidget.h"

#include <iostream>
#include <iterator>
#include <ostream>
#include <qapplication.h>
#include <random>
#include <string>
#include <unordered_map>
#include <vector>

int main(int argc, char *argv[]) {
  std::cout << "nmsl " << std::endl;
  QApplication app(argc, argv);

  // 测试main窗口
  mainwidget w;
  w.show();

  std::random_device dev;
  std::uniform_int_distribution<> dist(0, 20000); //[v1,v2)
  std::vector<std::string> events = {"zxnoter😰", "精灵幻想记!😊", "hyp🤬"};
  std::unordered_map<int, int> indicies = {{0,0},{1,0},{2,0}};
  for (int i = 0; i < 100000000; i++)
    indicies[dist(dev) %3] ++;
  for (auto& pair: indicies) 
    std::cout <<"value:["+events[pair.first]+"]->"<< "counts:["+std::to_string(pair.second)+"]"<< std::endl;

  // 进入qt事件循环
  return app.exec();
}
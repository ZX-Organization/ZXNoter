#include <qt/ui/mainwidget.h>

#include <iostream>
#include <iterator>
#include <ostream>
#include <qapplication.h>
#include <random>
#include <string>
#include <unordered_map>
#include <vector>
#include <fstream>
#include <filesystem>

int main(int argc, char *argv[]) {
    std::cout << "nmsl " << std::endl;
    QApplication app(argc, argv);

    // 测试main窗口
    mainwidget w;
    w.show();

    std::random_device dev;
    std::uniform_int_distribution<> dist(0, 20000); //[v1,v2)
    std::vector<std::string> events = {"zxnoter😰", "精灵幻想记!😊", "hyp🤬"};
    std::unordered_map<int, int> indicies = {{0, 0},
                                             {1, 0},
                                             {2, 0}};


    std::filesystem::path filePath = "D:\\VMware\\Win10\\LTSC\\Windows 10 x64-000002-s008.vmdk";
    uintmax_t fsize = std::filesystem::file_size(filePath);
    std::cout << "fs:[" + std::to_string(fsize) + "]" << std::endl;
    std::ifstream is;
    is.open(filePath, std::ios::binary);
    char *fs = new char[fsize];

    uint64_t count = 0;
    while (true) {
        is.read(fs + count, 4096);
        std::streamsize bytesRead = is.gcount();
        count += bytesRead;
        if (bytesRead == 0)
            break;
    }
    std::cout << "rd:[" + std::to_string(count) + "]" << std::endl;

    auto ufs = reinterpret_cast<uint8_t *>(fs);
    is.close();

    std::vector<int> vec(10000000);
    for (int i = 0; i < 10000000; i++) {
        int v = ufs[i % fsize];
        if (v == 0)
            continue;
        indicies[v % 3]++;
    }

    delete[] ufs;


    for (auto &pair: indicies)
        std::cout << "value:[" + events[pair.first] + "]->" << "counts:[" + std::to_string(pair.second) + "]"
                  << std::endl;

    // 进入qt事件循环
    return app.exec();
}
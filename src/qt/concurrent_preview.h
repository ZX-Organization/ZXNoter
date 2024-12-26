//
// Created by zedoC on 2024/12/26.
//

#ifndef ZXNOTER_CPP_CONCURRENT_PREVIEW_H
#define ZXNOTER_CPP_CONCURRENT_PREVIEW_H

#include <thread>
#include <condition_variable>
#include <filesystem>
#include <qapplication.h>
#include <iostream>
#include <fstream>


class ConcurrentPreview {
public:
    ConcurrentPreview(QApplication *app, std::filesystem::path *path);

    ~ConcurrentPreview();

    ///开始监听
    void Start();

    ///停止监听
    void Stop();

    ///设置新监听文件
    inline void const SetPath(const std::filesystem::path *&new_path) {
        path = new_path;
    };

    ///周期(ms)
    uint32_t interval{200};

    inline bool const isWatching() {
        return watching;
    };

private:

    void watch();

    QApplication *app;
    const std::filesystem::path *path;
    uint64_t modifiedTime;
    bool watching{false};
    std::thread thread;
};


#endif //ZXNOTER_CPP_CONCURRENT_PREVIEW_H

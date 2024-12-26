//
// Created by zedoC on 2024/12/26.
//

#ifndef ZXNOTER_CPP_CONCURRENT_PREVIEW_H
#define ZXNOTER_CPP_CONCURRENT_PREVIEW_H


#include <condition_variable>
#include <filesystem>
#include <qapplication.h>

class ConcurrentPreview {
public:
    ConcurrentPreview(std::filesystem::path *path, QApplication *app);

    ConcurrentPreview(QApplication *app);

    bool setWatchFilePath(std::filesystem::path &qss_file);

    ///周期(ms)
    uint32_t interval{200};

    inline bool const isWatching() {
        return watching;
    };

private:
    void watch(std::filesystem::path *path, QApplication *app);

    std::chrono::time_point<std::filesystem::__file_clock> modifiedTime;
    std::filesystem::path qss_path;
    bool watching{false};
    std::thread thread;
};


#endif //ZXNOTER_CPP_CONCURRENT_PREVIEW_H

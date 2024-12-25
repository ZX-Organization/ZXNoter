cmake_minimum_required(VERSION 3.28)

# 定义项目
project(ZXNoter-cpp LANGUAGES CXX)

# 自动处理qt文件
set(CMAKE_AUTOUIC ON)
set(CMAKE_AUTOMOC ON)
set(CMAKE_AUTORCC ON)

# 设置c++标准
set(CMAKE_CXX_STANDARD 23)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
# 导出编译命令(clangd补全用)
set(CMAKE_EXPORT_COMPILE_COMMANDS 1)

# 包含src中全部头文件
include_directories(src)

# 分系统find库
if(WIN32)
# WIN系统
    set(VCPKG "$ENV{CMAKE_TOOLCHAIN_FILE}/../../../packages/")
    # 包含外部头文件
    list(APPEND CMAKE_MODULE_PATH "${VCPKG}ffmpeg_x64-windows/share/ffmpeg")
    list(APPEND CMAKE_PREFIX_PATH "${VCPKG}sdl2_x64-windows/share/sdl2")
    list(APPEND CMAKE_PREFIX_PATH "${VCPKG}jsoncpp_x64-windows/share/jsoncpp")
    find_package(FFMPEG REQUIRED)
    find_package(SDL2 CONFIG REQUIRED)
endif(WIN32)
if(UNIX)
# UNIX系统
    # 查找sdl2
    find_package(SDL2 CONFIG REQUIRED)
endif(UNIX)

# 查找jsoncpp库
find_package(jsoncpp CONFIG REQUIRED)
# 查找Qt库
find_package(Qt6 REQUIRED COMPONENTS Gui Core Widgets OpenGL OpenGLWidgets)

# qt ui文件列表
set(QTUIS 
    src/qt/ui/mainwidget.ui)
# 源文件列表
set(SOURCES 
    src/qt/gl/glcanvas.cpp
    src/qt/ui/mainwidget.cpp
    src/main.cpp
    ${QTUIS})

# 添加启动项
add_executable(test_zxnoter ${SOURCES})

# 链接Qt库
target_link_libraries(test_zxnoter PRIVATE 
    Qt::Core
    Qt::Gui 
    Qt::Widgets 
    Qt::OpenGL 
    Qt::OpenGLWidgets)

# 链接零散库
target_link_libraries(
    test_zxnoter PRIVATE 
    JsonCpp::JsonCpp 
    SDL2::SDL2
    )
# 链接sdl2库
target_link_libraries(test_zxnoter PRIVATE SDL2::SDL2)
# 链接ffmpeg
if(WIN32)
# WIN系统
    # 包含ffmpeg头文件
    include_directories(${FFMPEG_INCLUDE_DIRS})
    target_link_libraries(test_zxnoter PRIVATE ${FFMPEG_LIBRARIES})
endif(WIN32)
if(UNIX)
# UNIX系统
    # 查找包管理器
    find_package(PkgConfig REQUIRED)
    # 直接查找ffmpeg
    pkg_check_modules(
        ffmpeg
        REQUIRED
        IMPORTED_TARGETS
        libavutil libavformat libavcodec libswresample libswscale
    )
    # 链接ffmpeg
    target_link_libraries(test_zxnoter PRIVATE PkgConfig::ffmpeg)
endif(UNIX)
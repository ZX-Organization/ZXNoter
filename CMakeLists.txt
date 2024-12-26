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
  # 配置vcpkg库路径
  list(APPEND CMAKE_MODULE_PATH "${VCPKG}ffmpeg_x64-windows/share/ffmpeg")
  # sdl2(simple directmedia layer)媒体库
  list(APPEND CMAKE_PREFIX_PATH "${VCPKG}sdl2_x64-windows/share/sdl2")
  # json库
  list(APPEND CMAKE_PREFIX_PATH "${VCPKG}jsoncpp_x64-windows/share/jsoncpp")
  # gl数学库
  list(APPEND CMAKE_PREFIX_PATH "${VCPKG}glm_x64-windows/share/glm")
  # 字体库
  list(APPEND CMAKE_PREFIX_PATH "${VCPKG}freetype_x64-windows/share/freetype")
  # 日志和前置
  list(APPEND CMAKE_PREFIX_PATH "${VCPKG}fmt_x64-windows/share/fmt")
  list(APPEND CMAKE_PREFIX_PATH "${VCPKG}spdlog_x64-windows/share/spdlog")
  find_package(FFMPEG REQUIRED)
  find_package(SDL2 CONFIG REQUIRED)
  find_package(glm CONFIG REQUIRED)
  find_package(Freetype CONFIG REQUIRED)
  find_package(spdlog CONFIG REQUIRED)
endif(WIN32)
if(UNIX)
  # UNIX系统
  find_package(SDL2 REQUIRED)
  find_package(glm REQUIRED)
  find_package(Freetype REQUIRED)
  find_package(spdlog REQUIRED)
endif(UNIX)

# 查找jsoncpp库
find_package(jsoncpp CONFIG REQUIRED)
# 查找Qt库
find_package(Qt6 REQUIRED COMPONENTS Gui Core Widgets OpenGL OpenGLWidgets)

# qt ui文件列表
set(QTUIS src/qt/ui/mainwindow.ui)
# qt资源文件管理
set(RESOURCES src/qt/ui/css_resources.qrc)
# 源文件列表
set(SOURCES src/qt/gl/glcanvas.cpp src/qt/ui/mainwindow.cpp src/main.cpp
            ${QTUIS} ${RESOURCES})

# 添加启动项
add_executable(test_zxnoter ${SOURCES})

# 链接Qt库
target_link_libraries(test_zxnoter PRIVATE Qt::Core Qt::Gui Qt::Widgets
                                           Qt::OpenGL Qt::OpenGLWidgets)

# 链接零散库
target_link_libraries(test_zxnoter PRIVATE JsonCpp::JsonCpp SDL2::SDL2 glm::glm
                                           Freetype::Freetype spdlog::spdlog)
# 链接ffmpeg
if(WIN32)
  # WIN系统 包含ffmpeg头文件
  include_directories(${FFMPEG_INCLUDE_DIRS})
  target_link_libraries(test_zxnoter PRIVATE ${FFMPEG_LIBRARIES})
endif(WIN32)
if(UNIX)
  # UNIX系统 查找包管理器
  find_package(PkgConfig REQUIRED)
  # 直接查找ffmpeg
  pkg_check_modules(
    ffmpeg
    REQUIRED
    IMPORTED_TARGET
    libavutil
    libavformat
    libavcodec
    libswresample
    libswscale)
  # 链接ffmpeg
  target_link_libraries(test_zxnoter PRIVATE PkgConfig::ffmpeg)
endif(UNIX)


#include "mainwidget.h"
#include "ui_mainwidget.h"

mainwidget::mainwidget(QWidget *parent)
    : QWidget(parent), ui(new Ui::mainwidget) {
  ui->setupUi(this);
  // 取得glcanvas对象指针
  auto openglcanvas = ui->openGLWidget;
}

mainwidget::~mainwidget() { delete ui; }

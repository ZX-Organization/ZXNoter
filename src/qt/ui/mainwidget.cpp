#include "mainwidget.h"
#include "ui_mainwidget.h"

mainwidget::mainwidget(QWidget *parent)
    : QWidget(parent), ui(new Ui::mainwidget) {
  ui->setupUi(this);
}

mainwidget::~mainwidget() { delete ui; }

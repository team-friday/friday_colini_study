from PyQt5 import QtWidgets
from PyQt5.QtWidgets import QWidget, QTextEdit


class View(QWidget):
    def __init__(self, parent=None):
        self.widgetList = []
        self.window = QtWidgets.QWidget()
        self.layout = QtWidgets.QHBoxLayout()
        self.text_area = None

    def set_title(self, title):
        self.window.setWindowTitle(title)

    def set_size(self, x, y, a, b):
        self.window.setGeometry(x, y, a, b)

    def add_text_area(self):
        self.text_area = QtWidgets.QTextEdit()
        self.text_area.setFixedSize(600, 400)
        self.text_area.setLineWrapMode(QTextEdit.NoWrap)
        self.text_area.setReadOnly(True)
        self.widgetList.append(self.text_area)
        return self.text_area

    def get_text_area(self):
        return self.text_area

    def add_btn(self, name):
        qt_button = QtWidgets.QPushButton(name)
        self.widgetList.append(qt_button)
        return qt_button

    def add_radio_btn(self, name):
        qt_radio_button = QtWidgets.QRadioButton(name)
        self.widgetList.append(qt_radio_button)
        return qt_radio_button

    def add_edit(self, name):
        qt_edit = QtWidgets.QLineEdit(name)
        qt_edit.setFixedWidth(200)
        self.widgetList.append(qt_edit)
        return qt_edit

    def add_label(self, name):
        qt_label = QtWidgets.QLabel(name)
        self.widgetList.append(qt_label)
        return qt_label

    def add_group_layout(self, name):
        group_box = QtWidgets.QGroupBox(name)
        protocol_box = QtWidgets.QVBoxLayout()

        for widget in self.widgetList:
            protocol_box.addWidget(widget)

        group_box.setLayout(protocol_box)
        self.widgetList.clear()
        self.layout.addWidget(group_box)
        return group_box

    def show(self):
        self.window.setLayout(self.layout)
        self.window.show()
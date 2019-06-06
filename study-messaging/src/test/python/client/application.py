# pip install -r requirements.txt

import sys
import time
from threading import Thread

from PyQt5 import QtWidgets
from PyQt5.QtCore import QProcess
from PyQt5.QtWidgets import QWidget
from view import View
from render import render


def components_set(view):
    render(view)


if __name__ == "__main__":
    app = QtWidgets.QApplication(sys.argv)

    view = View()
    view.set_size(300, 200, 700, 300)
    view.set_title("Study Client")
    components_set(view)
    view.show()
    sys.exit(app.exec_())

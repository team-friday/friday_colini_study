import sys
import datetime
import logging

from PyQt5.QtCore import QThread
from PyQt5.QtGui import QTextCursor
from PyQt5.uic.properties import QtGui
from client import WsClient

client = None
send_text = None
ip_edit = None
port_edit = None

log_text = None

logger = logging.getLogger(name='study')


class QtHandler(logging.Handler):

    def __init__(self):
        logging.Handler.__init__(self)

    def emit(self, record):
        now = datetime.datetime.now()
        now_date = now.strftime('[%Y-%m-%d %H:%M:%S] ')
        record = self.format(record)
        # text_area.moveCursor(QTextCursor.End)
        log_text.insertPlainText(now_date + record + '\n')
        # text_area.appendPlainText(now_date + record)


handler = QtHandler()
handler.setFormatter(logging.Formatter("%(levelname)s: %(message)s"))
logger.addHandler(handler)
logger.setLevel(logging.DEBUG)


def connection_function():
    global client
    client = WsClient(ip_edit.text(), port_edit.text())
    client.connect()
    return client


def request_message_function():
    global client
    client.request_message(send_text.toPlainText())


def close_function():
    global client
    client.close()


def render(view):
    global ip_edit, port_edit, send_text, log_text;

    view.add_label("IP")
    ip_edit = view.add_edit("localhost")

    view.add_label("PORT")
    port_edit = view.add_edit("8081")

    view.add_label("message")
    send_text = view.add_text_area()
    send_text.setReadOnly(False)

    info_box = view.add_group_layout("info")
    info_box.resize(50, 80)

    connect_btn = view.add_btn("connect")
    request_btn = view.add_btn("request")
    close_btn = view.add_btn("close")

    view.add_group_layout("protocol")
    connect_btn.clicked.connect(lambda: connection_function())
    request_btn.clicked.connect(lambda: request_message_function())
    close_btn.clicked.connect(lambda: close_function())

    log_text = view.add_text_area()
    log_text.setReadOnly(True)
    view.add_group_layout("log")

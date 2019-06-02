import logging
from threading import Thread
from twisted.internet.defer import Deferred
from twisted.internet import protocol
from twisted.internet import reactor
from autobahn.twisted.websocket import WebSocketClientFactory
from protocol import V3WebSocketProtocol

logger = logging.getLogger('study')
_twisted_thread = None


class StudyWebSocketProtocolFactory(WebSocketClientFactory):
    protocol = V3WebSocketProtocol


class WsClient(object):

    def __init__(self, host, port):
        self.host = host
        self.port = port
        self.factory = None

    def connect(self):
        self.factory = StudyWebSocketProtocolFactory(
            u"ws://{0}:{1}/study/message/v1".format(self.host, self.port)
        )
        reactor.connectTCP(self.host, int(self.port), self.factory)

        global _twisted_thread
        if _twisted_thread is None:
            _twisted_thread = Thread(target=lambda: reactor.run(installSignalHandlers=False), name="Ws_Twisted")
            _twisted_thread.setDaemon(True)
            _twisted_thread.start()

        return self.factory

    def request_message(self, message):
        reactor.callFromThread(self.factory.protocol.reqeuset_message, message)

    def close(self):
        reactor.callFromThread(self.factory.protocol.close)

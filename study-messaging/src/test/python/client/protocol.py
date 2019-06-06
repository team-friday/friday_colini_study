import logging
import hexdump
import json
import uuid
from twisted.internet.defer import Deferred
from autobahn.twisted.websocket import WebSocketClientProtocol

logger = logging.getLogger('study')
transport = None


class WebSocketProtocolProvider(object):
    @staticmethod
    def mak_send_json(message):
        data = {
            "type": "SEND",
            "createdAt": "20190502101125",
            "expired": "20191102101125",
            "message": message,
            "userName": "K",
            "messageId": str(uuid.uuid4()),
            "channelId": 1
        }
        json_data = json.dumps(data)
        return json_data

    @staticmethod
    def make_join_json():
        data = {
            "type": "JOIN",
            "createdAt": "20190502101125",
            "expired": "20191102101125",
            "userName": "K",
            "messageId": str(uuid.uuid4()),
            "channelId": 1
        }
        json_data = json.dumps(data)
        return json_data

    @staticmethod
    def make_left_json():
        data = {
            "type": "LEFT",
            "createdAt": "20190502101125",
            "expired": "20191102101125",
            "userName": "K",
            "messageId": str(uuid.uuid4()),
            "channelId": 1
        }
        json_data = json.dumps(data)
        return json_data

class V3WebSocketProtocol(WebSocketClientProtocol):
    def onConnect(self, response):
        global transport
        transport = self
        logger.info("client connected: {0}".format(response.peer))

    def onOpen(self):
        logger.info("WebSocket connection open.")
        data = WebSocketProtocolProvider.make_join_json()
        transport.sendMessage(data.encode('utf-8'))

    def onMessage(self, payload, isBinary):
        """
         Wrapping WebsocketClientProtocol Deferred callback, Not Working Deferred
        :param payload: String
        :param isBinary: Boolean
        :return:
        """
        if not isBinary:
            self.data_received_callback(payload)

    def onClose(self, wasClean, code, reason):
        logger.info("WebSocket connection closed: {0}".format(reason))


    @staticmethod
    def reqeuset_message(message):
        global transport
        data = WebSocketProtocolProvider.mak_send_json(message)
        transport.sendMessage(data.encode('utf-8'))

    @staticmethod
    def close():
        data = WebSocketProtocolProvider.make_left_json()
        transport.sendMessage(data.encode('utf-8'))
        transport.transport.loseConnection()

    @staticmethod
    def data_received_callback(payload):
        logger.info("Text message received: {0}".format(payload.decode('utf8')))


    @staticmethod
    def data_err_callback(err):
        logger.info("err:" + err)

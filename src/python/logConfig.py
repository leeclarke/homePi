import logging, os
from logging import FileHandler, StreamHandler

default_formatter = logging.Formatter(\
   "[%(asctime)s] %(name)s-%(levelname)s: %(message)s")

default_log_file = os.path.join(os.path.dirname(os.path.abspath(__file__)), "log",'homePiRun.log')

console_handler = StreamHandler()
console_handler.setFormatter(default_formatter)

error_handler = FileHandler(default_log_file, "a")
error_handler.setLevel(logging.DEBUG)
error_handler.setFormatter(default_formatter)

root = logging.getLogger()
root.addHandler(console_handler)
root.addHandler(error_handler)
root.setLevel(logging.DEBUG)

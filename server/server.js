// server.js
const express = require('express');
const http = require('http');
const cors = require('cors');
const { Server } = require('socket.io');

const app = express();
app.use(cors());                      // allow cross-origin

const server = http.createServer(app);
const io = new Server(server, {
  cors: { origin: '*' }
});

io.on('connection', socket => {

  console.log('Client connected:', socket.id);

  // When a message arrives, broadcast it to everyone
  socket.on('chat-message', msg => {
        console.log(`New message from ${socket.id}: ${msg}`);
    io.emit('chat-message', msg);
  });

  socket.on('disconnect', () => {
    console.log('Client disconnected:', socket.id);
  });
});

const PORT = 3000;
server.listen(PORT, () => {
  console.log(`Chat server running on http://localhost:${PORT}`);
});
 
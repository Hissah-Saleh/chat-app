// server.js
require('dotenv').config();
const express   = require('express');
const app       = express();
const path      = require('path');
const fs        = require('fs');
const http      = require('http');
const server    = http.createServer(app);
const io        = require('socket.io')(server);
const multer    = require('multer');
const { v4: uuid } = require('uuid');

const PORT = process.env.PORT || 3000;

// ─── Static ──────────────────────────────────────────────────────────────────
app.use(express.static(path.join(__dirname, 'public')));
let numUsers = 0;

app.use('/uploads', express.static(path.join(__dirname, 'uploads')));

// ─── Multer setup ────────────────────────────────────────────────────────────
const UPLOAD_DIR = path.join(__dirname, 'uploads');
if (!fs.existsSync(UPLOAD_DIR)) fs.mkdirSync(UPLOAD_DIR, { recursive: true });

const ALLOWED = [
  'image/jpeg','image/png','image/gif',
  'video/mp4','video/webm',
  'audio/mpeg','audio/mp3','audio/x-mpeg','audio/x-wav','audio/wav'
];

const storage = multer.diskStorage({
  destination: (_req, _file, cb) => cb(null, UPLOAD_DIR),
  filename: (_req, file, cb) => {
    const name = uuid() + path.extname(file.originalname);
    console.log(`[upload] naming file: ${name}`);
    cb(null, name);
  }
});

const upload = multer({
  storage,
  limits: { fileSize: 20 * 1024 * 1024 }, // 20MB max
  fileFilter: (_req, file, cb) => {
    const ok = ALLOWED.includes(file.mimetype);
    console.log(`[upload] filter "${file.originalname}" as ${ok ? 'ACCEPT' : 'REJECT'}`);
    cb(ok ? null : new Error('Unsupported media type'), ok);
  }
});

// ─── Upload endpoint ────────────────────────────────────────────────────────
app.post('/upload', upload.single('media'), (req, res) => {
  console.log('[upload] request received');
  if (!req.file) {
    console.log('[upload] no file found');
    return res.status(400).json({ error: 'No file uploaded' });
  }

  const username = req.body.username || 'unknown';
  const fileUrl  = `/uploads/${req.file.filename}`;
  console.log(`[upload] success: user="${username}", file="${req.file.filename}"`);

  const payload = {
    username,
    time:   new Date().toISOString(),
    media: {
      url:  fileUrl,
      type: req.file.mimetype,
      name: req.file.originalname
    }
  };

  io.emit('message', payload);
  console.log('[upload] emitted "message" with media');
  res.json({ success: true, url: fileUrl });
});

// ─── Socket.IO chatroom ─────────────────────────────────────────────────────
io.on('connection', (socket) => {
  console.log(`[socket] connect: id=${socket.id}`);
  let addedUser = false;

  socket.on('add user', (username) => {
            // io.emit('numUsers', io.engine.clientsCount);

    if (addedUser) {
      console.log(`[socket] add user ignored (already added): id=${socket.id} , activeUsers=${io.engine.clientsCount}`);
      return;
    }

        ++numUsers;

    io.emit('numUsers',  io.of('/').sockets.size);


    socket.username = username;
    addedUser = true;
    console.log(`[socket] add user: id=${socket.id}, username="${username}", activeUsers=${io.engine.clientsCount}`);
    socket.emit('login');
    //     const count = io.of("/").sockets.size;
    // io.emit('numUsers', count);

    io.emit('user joined', { username });

    
  });



io.on('connection', socket => {
  socket.on('getNumUsers', ack => {
    // ack is the function you passed from the client
    const count = io.of('/').sockets.size;
    ack(count);
  });
});

  socket.on('message', (text) => {
    if (!addedUser) {
      console.log(`[socket] message ignored (not added): id=${socket.id}`);
      return;
    }
    console.log(`[socket] message from="${socket.username}": "${text}"`);
    const payload = {
      username: socket.username,
      time:     new Date().toISOString(),
      text
    };
    io.emit('message', payload);
    console.log('[socket] emitted "message" with text');
  });

  socket.on('typing', () => {
    if (!addedUser) return;
    console.log(`[socket] typing: username="${socket.username}"`);
    // io.emit('typing', { username: socket.username });
        socket.broadcast.emit('typing');

  });

  socket.on('stop typing', () => {
    if (!addedUser) return;
    console.log(`[socket] stop typing: username="${socket.username}"`);
    // io.emit('stop typing', { username: socket.username });
        socket.broadcast.emit('stop typing');

  });

  socket.on('disconnect', () => {
        // io.emit('numUsers', io.engine.clientsCount);

    console.log(`[socket] disconnect: id=${socket.id}, username="${socket.username}, activeUsers=${io.engine.clientsCount}"`);
    if (addedUser) {

      io.emit('user left', { username: socket.username });

          // const count = io.of("/").sockets.size;
    io.emit('numUsers',  io.of('/').sockets.size);
      console.log('[socket] emitted "user left"');
    }
  });
});

// ─── Start server ────────────────────────────────────────────────────────────
server.listen(PORT, () => {
  console.log(`Server listening on port ${PORT}`);
});

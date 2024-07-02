import fs from 'fs';

const file = './dist/index.html';

const data = fs.readFileSync(file);
const fd = fs.openSync(file, 'w+');
const buffer = Buffer.from("<%@ page session=\"false\" %>\n");

fs.writeSync(fd, buffer, 0, buffer.length, 0);
fs.writeSync(fd, data, 0, data.length, buffer.length);
fs.closeSync(fd);

console.log("Post build script executed successfully!");

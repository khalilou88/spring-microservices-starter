module.exports = {
  randomString: randomString
};

function randomString() {
  return Math.random().toString(36).substring(2, 15);
}
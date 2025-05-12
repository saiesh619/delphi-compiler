const output = document.getElementById("output");
 
function log(msg) {
  output.textContent += msg + "\n";
}
 
const imports = {
  env: {
    printf: (fmtPtr, ...args) => {
      log(args[0]); // assumes "%d\n"
      return 0;
    },
    read_int: () => {
      const val = prompt("Enter an integer:");
      return parseInt(val || "0", 10);
    },
  }
};
 
(async () => {
  const response = await fetch("output.wasm");
  const buffer = await response.arrayBuffer();
  const { instance } = await WebAssembly.instantiate(buffer, imports);
  if (instance.exports.main) instance.exports.main();
})();
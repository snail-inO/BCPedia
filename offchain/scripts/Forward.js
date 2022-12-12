import { createRequire } from "module";
const require = createRequire(import.meta.url);

const { time } = require("@openzeppelin/test-helpers");

const args = process.argv;
if (args.length > 2) {
  if (args[2] == "time") {
    await time.increase(time.duration.hours(2));
    console.log("Time increased 2 hours");
  } else if (args[2] == "block") {
    if (args.length != 4) {
      console.log("Invalid argumetns");
    } else {
      await time.advanceBlockTo(args[3]);
      console.log("Block advanced to " + args[3]);
    }
  } else {
    console.log("Invalid arguments");
  }
} else {
  console.log("Invalid argumetns");
}

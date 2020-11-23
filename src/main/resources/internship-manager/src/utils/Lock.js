export default class Lock {

  //
  // Constructors
  //

  constructor() {
    this.locked = false;
  }

  //
  // Services
  //

  change(locked) {
    let last = this.locked;
    this.locked = locked;
    return last;
  }

  lock() {
    return this.change(true);
  }

  unlock() {
    return this.change(false);
  }
}
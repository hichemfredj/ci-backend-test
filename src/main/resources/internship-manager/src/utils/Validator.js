
class Validator {

  //
  // Utils
  //

  mapBackendError(errors, field){
    for(let error of Object.values(errors.response.data.errors))
      if(error.field == field)
        return error.defaultMessage;
    return '';
  }

  hasErrors(value) {
    for (let error of Object.values(value))
      if (error) return true;
    return false;
  }

  clearErrors(value) {
    for (let key of Object.keys(value))
      value[key] = '';
  }


  //
  // Validators
  //

  email(value, message) {
    const regex = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

    return regex.test(value.toLowerCase()) ? '' : message;
  }

  size(value, min, max, message) {
    return value.length >= min && value.length <= max ? '' : message;
  }

  match(value1, value2, message){
    return value1 == value2 ? '' : message;
  }

  before(value, date, message){
    return value.getTime() < date ? '' : message;
  }

  after(value, date, message){
    return value.getTime() >= date ? '' : message;
  }

  week(value, message){
    return value >= 7 ? '' : message;
  }

  notBlank(value, message) {
    return value ? '' : message;
  }

  min(value, min, message){
    return value >= min ? '' : message;
  }

  max(value, max, message){
    return value <= max ? '' : message;
  }
  
  positive(value, message){
    return value >= 0 ? '' : message;
  }

  negative(value, message){
    return value < 0 ? '' : message;
  }

}

export default new Validator();
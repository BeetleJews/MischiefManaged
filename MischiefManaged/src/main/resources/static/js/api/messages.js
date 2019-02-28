import Vue from 'vue'

const messages = Vue.resource('/message{/id}')      /*Во многих естах используется resource,
                                                        поэтому что бы не создавать его каждый раз,
                                                        мы его вынесли в отдельный класс*/

export default {
    add: message => messages.save({}, message),     /*отправляя объект на сохранение не указываем id тк мы его не знаем*/
    update: message => messages.update({id: message.id}, message),
    remove: id => messages.remove({id})     /*короткая запимь id: id */

}
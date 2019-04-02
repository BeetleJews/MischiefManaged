import Vue from 'vue'

const comments = Vue.resource('/comment{/id}')      /*Во многих местах используется resource,
                                                        поэтому что бы не создавать его каждый раз,
                                                        мы его вынесли в отдельный класс*/

export default {
    add: comment => comments.save({}, comment),     /*отправляя объект на сохранение не указываем id тк мы его не знаем*/
}
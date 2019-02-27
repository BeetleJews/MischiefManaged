

export function getIndex(list, id) {        //определяет индекс элемента в колекции
        for(var i = 0; i < list.length; i++) {
            if (list[i].id === id) {
                return i
            }
        }
        return -1
    }
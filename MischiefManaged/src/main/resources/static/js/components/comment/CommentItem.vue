<template>
    <v-list-tile class="grey lighten-4">
        <v-list-tile-avatar>
            <img :src="comment.author.userpic">
        </v-list-tile-avatar>

        <v-list-tile-content>
            <v-list-tile-title>{{comment.author.name}}</v-list-tile-title>



            <v-list-tile-sub-title>
                    {{comment.text}}
            </v-list-tile-sub-title>

            <v-list-tile-sub-title>
                <span class="caption">{{comment.creationDate}}</span>
            </v-list-tile-sub-title>
        </v-list-tile-content>


    </v-list-tile>
</template>

<script>
    export default {
        name: 'CommentItem',
        props: ['comment']
    }
</script>

<style scoped>
</style>
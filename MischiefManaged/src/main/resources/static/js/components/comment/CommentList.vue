<template>
    <v-list three-line>
        <template
                v-for="(item, index) in comments"
        >
            <v-divider></v-divider>

            <comment-item
                    :comment="item"
                    :key="'item' + index"
            ></comment-item>
        </template>

        <v-divider></v-divider>


        <comment-form :message-id="messageId" class="pt-2"></comment-form>
    </v-list>
</template>

<script>
    import CommentForm from './CommentForm.vue'
    import CommentItem from './CommentItem.vue'
    export default {
        name: 'CommentList',
        components: {CommentForm, CommentItem},
        props: ['comments', 'messageId']
    }
</script>

<style scoped>
</style>
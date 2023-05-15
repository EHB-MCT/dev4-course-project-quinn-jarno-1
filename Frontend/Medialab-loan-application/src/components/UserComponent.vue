
<script>

    export default {
        name:"UserComponent",
        props: {
            username: String,
            email: String,
            role: String
        }
    }
</script>

<template>
    <div class="user">
        <h4>{{ username }}</h4>
        <h4>{{ email }}</h4>
        <h4>{{ role }}</h4>
    </div>
</template>

<style scoped>
    .user {
        display: flex;
        align-items: center;
    }

    .user > * {
        padding: 12px;
    }
</style>
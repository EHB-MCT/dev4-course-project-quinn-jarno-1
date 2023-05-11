<!-- EEN ITEM COMPONENT -->

<script>
    export default {
        props: {
            name: String,
            description: String,
            isLoanedOut: Boolean,
        }
    }
</script>

<template>
    <div class="item">
        <h4>{{ name }}</h4>
        <p>{{ description }}</p>
        <p>{{ isLoanedOut }}</p>
    </div>
</template>

<style scoped>
    .item {
        display: flex;
        align-items: center;
    }

    .item > * {
        padding: 12px;
    }

</style>
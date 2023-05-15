<template>
    <div class="item">
        <h4>{{ name }}</h4>
        <h5>{{ description }}</h5>
        <p>{{ isLoanedOut }}</p>
    </div>
</template>

<script>
    export default {
        props: {
            name: String,
            description: String,
            isLoanedOut: Boolean
        }
    }
</script>

<style scoped>
    .item {
        display: flex;
        align-items: center;
    }

    .item > * {
        padding: 12px;
    }

</style>
import {
    createRouter,
    createWebHistory
} from 'vue-router'
import LoginComponent from '../components/LoginComponent.vue'

const router = createRouter({
    history: createWebHistory(
        import.meta.env.BASE_URL),
    routes: [{
            path: '/',
            name: 'login',
            component: LoginComponent
        },
        {
            path: '/items',
            name: 'items',
            component: () => import('../views/ItemView.vue'),
        },
        {
            path: '/admin',
            name: 'admin',
            component: () => import('../views/AdminView.vue'),
        },
    ]
})

export default router;
import {
    createRouter,
    createWebHistory
} from 'vue-router'
import ChatView from '../views/ChatView.vue'

const router = createRouter({
    history: createWebHistory(
        import.meta.env.BASE_URL),
    routes: [{
            path: '/',
            name: 'items',
            component: ChatView
        },
        {
            path: '/login',
            name: 'login',
            component: () => import('../views/AboutView.vue')
        },
        {
            path: '/admin',
            name: 'admin',
            component: () => import('../views/AboutView.vue')
        }
    ]
})

export default router
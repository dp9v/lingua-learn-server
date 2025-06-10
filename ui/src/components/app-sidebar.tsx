import { Home } from 'lucide-react';
import {
    Sidebar,
    SidebarContent,
    SidebarFooter,
    SidebarGroup,
    SidebarGroupContent,
    SidebarGroupLabel,
    SidebarMenu,
    SidebarMenuItem,
    SidebarRail,
} from '@/components/ui/sidebar';
import { AppSidebarNav } from '@/components/app-sidebar-nav.tsx';
import { AppSidebarUser } from '@/components/app-sidebar-user.tsx';

// This is sample data.
const items = [
    {
        title: 'Main',
        url: '/#',
        icon: Home,
    },
    // {
    //     title: 'Settings',
    //     url: '#',
    //     icon: Settings,
    // },
];

export function AppSidebar({ ...props }: React.ComponentProps<typeof Sidebar>) {
    return (
        <Sidebar collapsible={'icon'} {...props}>
            <SidebarContent>
                <SidebarGroup>
                    <SidebarGroupLabel>LinguaLearn</SidebarGroupLabel>
                    <SidebarGroupContent>
                        <AppSidebarNav items={items} />
                    </SidebarGroupContent>
                </SidebarGroup>
            </SidebarContent>
            <SidebarFooter>
                <SidebarMenu>
                    <SidebarMenuItem>
                        <AppSidebarUser />
                    </SidebarMenuItem>
                </SidebarMenu>
            </SidebarFooter>
            <SidebarRail />
        </Sidebar>
    );
}

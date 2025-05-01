import {
    Sidebar,
    SidebarHeader,
    SidebarMenuButton,
    SidebarMenuItem,
    SidebarProvider,
} from '@/components/ui/sidebar.tsx';

export const MainLayout = () => {
    return (
        <SidebarProvider>
            <Sidebar>
                <SidebarHeader>
                    <SidebarMenuItem>
                        <SidebarMenuButton size="lg" asChild>
                            <a href="/">
                                <div className="flex aspect-square size-8 items-center justify-center rounded-lg bg-sidebar-primary text-sidebar-primary-foreground"></div>
                                <div className="flex flex-col gap-0.5 leading-none">
                                    <span className="font-semibold">LinguaLearn</span>
                                    <span className="">alfa</span>
                                </div>
                            </a>
                        </SidebarMenuButton>
                    </SidebarMenuItem>
                </SidebarHeader>
            </Sidebar>
        </SidebarProvider>
        // <div className="[--header-height:calc(theme(spacing.14))]">
        //     <MainHeader />
        //     <div className="flex flex-1">
        //         <main className="flex-1 p-6">
        //             <Outlet />
        //         </main>
        //     </div>
        // </div>
    );
};

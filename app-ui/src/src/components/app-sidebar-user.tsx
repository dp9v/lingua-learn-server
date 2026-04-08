import { useAuth } from '@/context/AuthContext.tsx';
import { useNavigate } from 'react-router-dom';
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu.tsx';
import { SidebarMenuButton } from '@/components/ui/sidebar.tsx';
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar.tsx';
import { LogOut } from 'lucide-react';

export const AppSidebarUser = () => {
    const { logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/');
    };

    return (
        <DropdownMenu>
            <DropdownMenuTrigger asChild>
                <SidebarMenuButton
                    size="lg"
                    className="data-[state=open]:bg-sidebar-accent data-[state=open]:text-sidebar-accent-foreground"
                >
                    <Avatar className="h-8 w-8 rounded-lg">
                        <AvatarImage src={''} alt={'Name'} />
                        <AvatarFallback className="rounded-lg">CN</AvatarFallback>
                    </Avatar>
                    <div className="flex items-center gap-3 p-2 cursor-pointer rounded-md hover:bg-muted transition">
                        <div className="flex flex-col">
                            <span className="text-sm font-medium">Name</span>
                            <span className="text-xs text-muted-foreground">email@email.com</span>
                        </div>
                    </div>
                </SidebarMenuButton>
            </DropdownMenuTrigger>
            <DropdownMenuContent
                className="w-[--radix-dropdown-menu-trigger-width] min-w-56 rounded-lg"
                side={'right'}
                align="end"
                sideOffset={4}
            >
                <DropdownMenuItem onClick={handleLogout} className="flex items-center gap-2 text-sm">
                    <LogOut className="h-4 w-4" />
                    Logout
                </DropdownMenuItem>
            </DropdownMenuContent>
        </DropdownMenu>
    );
};

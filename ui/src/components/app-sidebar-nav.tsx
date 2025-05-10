import { SidebarMenu, SidebarMenuButton, SidebarMenuItem } from '@/components/ui/sidebar.tsx';
import { LucideProps } from 'lucide-react';

interface AppSidebarNavProps {
    items: {
        title: string;
        url: string;
        icon: React.ForwardRefExoticComponent<Omit<LucideProps, 'ref'> & React.RefAttributes<SVGSVGElement>>;
    }[];
}

export const AppSidebarNav = ({ items }: AppSidebarNavProps) => {
    return (
        <SidebarMenu>
            {items.map(({ title, url, icon: Icon }) => (
                <SidebarMenuItem key={title}>
                    <SidebarMenuButton asChild>
                        <a href={url}>
                            <Icon />
                            <span>{title}</span>
                        </a>
                    </SidebarMenuButton>
                </SidebarMenuItem>
            ))}
        </SidebarMenu>
    );
};

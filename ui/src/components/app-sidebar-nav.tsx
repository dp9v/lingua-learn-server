import {SidebarMenu, SidebarMenuButton, SidebarMenuItem} from '@/components/ui/sidebar.tsx';
import {LucideProps} from 'lucide-react';
import {Link} from 'react-router-dom';

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
                        <Link to={url}>
                            {' '}
                            <Icon />
                            <span>{title}</span>
                        </Link>
                    </SidebarMenuButton>
                </SidebarMenuItem>
            ))}
        </SidebarMenu>
    );
};

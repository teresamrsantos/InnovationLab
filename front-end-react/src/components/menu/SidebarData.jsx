import React from 'react';
import * as AiIcons from 'react-icons/ai';
import * as BsIcons from 'react-icons/bs';
import * as FaIcons from 'react-icons/fa';
import { MdManageAccounts } from "react-icons/md";
export const SidebarData = [
  {
    title: ' Home',
    path: '/Home',
    icon: <AiIcons.AiFillHome />,
    cName: 'nav-text',

  },
  {
    title: ' My Profile',
    path: '/myprofile',
    icon: <BsIcons.BsPerson/>,
    cName: 'nav-text',
    submenu: [
      {
       title: " Edit Profile",
       path: '/editprofile',
      },
      {
       title: " Privacy",
       path: '/privacy',
      },
      {
       title: " Edit Password",
       path: '/editpassword',
      }
     ]
  },
  {
    title: ' My Posts',
    path: '/allMyIdeaNecessity',
    icon: <BsIcons.BsGrid />,
    cName: 'nav-text'
  },
  {
    title: ' My Projects',
    path: '/myprojects',
    icon: <BsIcons.BsInboxes />,
    cName: 'nav-text'
  },
  {
    title: ' Favorites',
    icon: <FaIcons.FaRegHeart />,
    cName: 'nav-text',
    submenu: [
      {
       title: " Favorite Projects",
       path: '/favprojects',
      },
      {
       title: " Favorite Posts",
       path: '/allFavoriteIdeaNecessity',
      }
     ]
  },
];

export const SidebarDataVisitor = [
  {
    title: ' Home',
    path: '/Home',
    icon: <AiIcons.AiFillHome />,
    cName: 'nav-text',

  },
  {
    title: ' My Profile',
    path: '/myprofile',
    icon: <BsIcons.BsPerson/>,
    cName: 'nav-text',
    submenu: [
      {
       title: " Edit Profile",
       path: '/editprofile',
      },
      {
       title: " Edit Password",
       path: '/editpassword',
      }
     ]
  },
     ]

     export const SidebarDataAdmin = [
      {
        title: ' Home',
        path: '/Home',
        icon: <AiIcons.AiFillHome />,
        cName: 'nav-text',
    
      },
      {
        title: ' My Profile',
        path: '/myprofile',
        icon: <BsIcons.BsPerson/>,
        cName: 'nav-text',
        submenu: [
          {
           title: " Edit Profile",
           path: '/editprofile',
          },
          {
           title: " Privacy",
           path: '/privacy',
          },
          {
           title: " Edit Password",
           path: '/editpassword',
          }
         ]
      },
      {
        title: ' My Posts',
        path: '/allMyIdeaNecessity',
        icon: <BsIcons.BsGrid />,
        cName: 'nav-text'
      },
      {
        title: ' My Projects',
        path: '/myprojects',
        icon: <BsIcons.BsInboxes />,
        cName: 'nav-text'
      },
      {
        title: ' Favorites',
        icon: <FaIcons.FaRegHeart />,
        cName: 'nav-text',
        submenu: [
          {
           title: " Favorite Projects",
           path: '/favprojects',
          },
          {
           title: " Favorite Posts",
           path: '/allFavoriteIdeaNecessity',
          }
         ]
      },
      {
        title: ' Manage Users',
        icon: <MdManageAccounts />,
        cName: 'nav-text',
        path: '/manageUsers',
      },
    ];
    
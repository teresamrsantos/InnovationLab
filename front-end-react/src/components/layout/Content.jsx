import React from "react";
import { Routes, Route } from "react-router-dom";
import Login from "../../views/initialPages/Login";
import Register from "../../views/initialPages/Register";
import NotFound from "../../views/initialPages/NotFound";
import RecoverPassword from "../../views/initialPages/RecoverPassword";
import ResetPassword from "../../views/initialPages/ResetPassword";
import ValidateAccount from "../../views/initialPages/ValidateAccount";
import EmailVerification from "../../views/initialPages/EmailVerification";
import AllIdeaNecessity from "../../views/Forum/AllIdeaNecessity";
import ChangePassword from "../../views/user/ChangePassword";
import EditUserProfile from "../../views/user/EditUserProfile";
import AllConversation from "../../views/Messages/AllConversation";
import NewConversation from "../../views/Messages/NewConversation";
import Conversation from "../../views/Messages/Conversation";
import AddIdeaNecesity from "../../views/Forum/AddIdeaNecessity"
import AddProject from "../../views/Projects/AddProjects"
import LinkIdeaNecessity from "../../views/Forum/LinkIdeaNecessity";
import PrivacyUserProfile from "../../views/user/PrivacyUserProfile"
import CommentIdeaNecessity from "../../views/Forum/CommentIdeaNecessity";
import PostRelatedIdeaNecessity from "../../views/Forum/PostRelatedIdeaNecessity";
import UsersAvailabilityIdeaNecessity from "../../views/Forum/UsersAvailabilityIdeaNecessity";
import AllMyIdeaNecessity from "../../views/Forum/AllMyIdeaNecessity";
import PostRelatedMyIdeaNecessity from "../../views/Forum/PostRelatedMyIdeaNecessity";
import AllFavoriteIdeaNecessity from "../../views/Forum/AllFavoriteIdeaNecessity";
import AllProjects from '../../views/Projects/AllProjects'
import ProjectDetails from '../../views/Projects/ProjectDetails'
import EditIdeaNecessity from "../../views/Forum/EditIdeaNecessity";
import MyProjects from '../../views/Projects/MyProjects'
import UserProfile from "../../views/user/UserProfile";
import EditProject from '../../views/Projects/EditProject'
import ManageUsersProject from '../../views/Projects/ManageUsersProject'
import AdminDashBoard from "../../views/user/AdminDashboard/AdminDashboard";
import MyFavoriteProjects from '../../views/Projects/MyFavoriteProjects';


const Content = props => (
    //https://blog.logrocket.com/build-rich-text-editor-lexical-react/
    <main >
        <Routes>
            <Route exact path="/" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/recoverPassword" element={<RecoverPassword />} />
            <Route path="/resetPassword/:resetToken" element={<ResetPassword />} />
            <Route path="/validateAccount/:id" element={<ValidateAccount />} />
            <Route path="/emailVerification" element={<EmailVerification />} />
            <Route path="/Home" element={<AllIdeaNecessity />} />
            <Route path="/editpassword" element={<ChangePassword />} />
            <Route path="/editprofile" element={<EditUserProfile />} />
            <Route path="/messages" element={<AllConversation />} />
            <Route path="/newconversation" element={<NewConversation />} />
            <Route path="/conversation/:id/:name" element={<Conversation />} />
            <Route path="/editprofile" element={<EditUserProfile />} />
            <Route path="/addIdea" element={<AddIdeaNecesity />} />
            <Route path="/addProject" element={<AddProject />} />
            <Route path="/linkIdeaNecessity/:id" element={<LinkIdeaNecessity />} />
            <Route path="/privacy" element={<PrivacyUserProfile />} />
            <Route path="/commentIdeaNecessity/:id" element={<CommentIdeaNecessity />} />
            <Route path="/postRelatedIdeaNecessity/:id" element={<PostRelatedIdeaNecessity />} />
            <Route path="/usersAvailabilityIdeaNecessity/:id" element={<UsersAvailabilityIdeaNecessity />} />
            <Route path="/allMyIdeaNecessity" element={<AllMyIdeaNecessity />} />
            <Route path="/postRelatedMyIdeaNecessity/:id" element={<PostRelatedMyIdeaNecessity />} />
            <Route path="/allFavoriteIdeaNecessity" element={<AllFavoriteIdeaNecessity />} />
            <Route path="/projects" element={<AllProjects />} />
            <Route path="/projectDetails/:id" element={<ProjectDetails />} />
            <Route path="/forum" element={<AllIdeaNecessity />} />
            <Route path="/editIdeaNecessity/:id" element={<EditIdeaNecessity />} />
            <Route path="/myprojects" element={<MyProjects />} />
            <Route path="/userProfile/:id" element={<UserProfile />} />
            <Route path="/editProject/:id" element={<EditProject />} />
            <Route path="/manageUsersProject/:id" element={<ManageUsersProject />} />
            <Route path="/favprojects" element={<MyFavoriteProjects />} />
            <Route path="/manageUsers" element={<AdminDashBoard />} />
            <Route path="/editprofile/:id" element={<EditUserProfile />} />
            <Route path="/*" element={<NotFound />} />
        </Routes>
    </main>
)

export default Content
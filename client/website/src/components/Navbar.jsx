import React, {Component} from 'react';
import { Menu, Typography } from 'antd';
import { Link } from 'react-router-dom';
import { HomeOutlined, MoneyCollectOutlined, FundOutlined } from '@ant-design/icons';

export default class Navbar extends Component {

    render() {
        return (
            <div className="nav-container">
                <div className="logo-container">
                    {/* <Avatar src={icon} size="large" /> */}
                    <Typography.Title level={2} className="logo"><Link to="/">Pet Tracker System</Link></Typography.Title>
                    {/* <Button className="menu-control-container" onClick={() => setActiveMenu(!activeMenu)}><MenuOutlined /></Button> */}
                </div>

                <Menu theme="dark">
                    <Menu.Item icon={<HomeOutlined />}>
                        <Link to="/">Home</Link>
                    </Menu.Item>

                    <Menu.Item icon={<FundOutlined />}>
                        <Link to="/register">Register New Pet</Link>
                    </Menu.Item>
                    
                    <Menu.Item icon={<MoneyCollectOutlined />}>
                        <Link to="/view">View All Pets</Link>
                    </Menu.Item>
                </Menu>
            </div>
        );
    }
};


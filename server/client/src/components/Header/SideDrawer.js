import React, { useCallback } from 'react';
import app from '../../firebase'

/** styling */
import clsx from 'clsx';
import useMediaQuery from '@material-ui/core/useMediaQuery';
import { makeStyles, useTheme } from '@material-ui/core/styles';

/** components from material UI */
import List from '@material-ui/core/List';
import Divider from '@material-ui/core/Divider';
import Drawer from '@material-ui/core/Drawer';
import IconButton from '@material-ui/core/IconButton';

/** icons */
import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';
import ChevronRightIcon from '@material-ui/icons/ChevronRight';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import ExitToAppIcon from '@material-ui/icons/ExitToApp';
import AppsIcon from '@material-ui/icons/Apps';
import LockOpenIcon from '@material-ui/icons/LockOpen';


const drawerWidth = 240;
const useStyles = makeStyles((theme) => ({
    drawer: {
        width: drawerWidth,
        flexShrink: 0,
        whiteSpace: 'nowrap',              
      }, 
      drawerOpen: {
        width: drawerWidth,
        transition: theme.transitions.create('width', {
          easing: theme.transitions.easing.sharp,
          duration: theme.transitions.duration.enteringScreen,
        }),
      },
      drawerClose: {
        transition: theme.transitions.create('width', {
          easing: theme.transitions.easing.sharp,
          duration: theme.transitions.duration.leavingScreen,
        }),
        overflowX: 'hidden',
        width: theme.spacing(7) + 1,
        [theme.breakpoints.up('sm')]: {
          width: theme.spacing(9) + 1,
        },
      },
      toolbar: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'flex-end',
        padding: theme.spacing(0, 1),
        // necessary for content to be below app bar
        ...theme.mixins.toolbar,
      }
}));

/** modify the drawer items here */
const drawerItems = [
  {
    icon: <ExitToAppIcon/>,
    ariaLabel: "Log out"
  },
  {
    icon: <LockOpenIcon/>,
    ariaLabel: "Premium"
  }

]

/** modify the tab items when screen size is below medium here */
const tabs = [
  {
    icon: <AppsIcon />,
    ariaLabel:"home"
  }
];

/** @param {*} props event handler and drawer state from Header component */
export default function SideDrawer({ ...props }) {
    const {
        drawerOpen,                
        handleDrawerClose
    } = props;

    const classes = useStyles();
    const theme = useTheme();
    
    /** detects if screen size reaches below medium */
    const isViewSizeBelowMedium = useMediaQuery(theme.breakpoints.down('md'));

    const handleSignOut = useCallback(async () => {        
      try {                  
          await app.auth().signOut();
          console.log("Signed out")
          window.location.href = '/';
      } catch (error) {
          console.log(`There is an error ${error}`);
      }
  }, [])

    return (
        <Drawer
            variant="permanent"
            className={clsx(classes.drawer, {
                [classes.drawerOpen]: drawerOpen,
                [classes.drawerClose]: !drawerOpen,
            })}
            classes={{
                paper: clsx({
                    [classes.drawerOpen]: drawerOpen,
                    [classes.drawerClose]: !drawerOpen,
                }),
            }}
        >
          <div className={classes.toolbar}>
              <IconButton onClick={handleDrawerClose}>
                  {theme.direction === 'rtl' ? <ChevronRightIcon /> : <ChevronLeftIcon />}
              </IconButton>
          </div>
          
          <Divider />

          <List>
            {drawerItems.map((item, index) => (
              <ListItem 
                button 
                key={`${item}${index}`}
                onClick={() => {
                  if(index === 0) {
                    console.log(index);
                    handleSignOut();
                  } else  window.location.href = '/limited';
                }}
                >
                <ListItemIcon>{item.icon}</ListItemIcon>
                <ListItemText primary={item.ariaLabel} />
              </ListItem>
            ))}
          </List>                

          { isViewSizeBelowMedium ? (
              <React.Fragment>
                <Divider />
                <List>
                  {tabs.map((tab, index) => (
                      <ListItem 
                        button 
                        key={`${tab}${index}`}
                        onClick={() => handleSignOut()}
                      >
                        <ListItemIcon>{tab.icon}</ListItemIcon>
                        <ListItemText primary={tab.ariaLabel} />
                      </ListItem>
                  ))}
                </List>   
              </React.Fragment>
          ) : null }             
        </Drawer>
    );
}
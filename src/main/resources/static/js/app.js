const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');

class App extends React.Component{
	constructor(props){
		super(props);
		this.state = {users:[]};
	}
	
	componentDidMount(){
		client({method:'GET',path:'/api/users'}).done(response=>{
		//	console.log(response.entity._embedded.users);
			this.setState({users:response.entity._embedded.users});	
		});
	}
	render(){
		//console.log(this.state.users);
		return (<UserList users = {this.state.users}/>)		
	}
}

class UserList extends React.Component{
	render(){
		//console.log(this.props.users);
		const users = this.props.users.map(user=>{
			//console.log(user);
			return (<User key={user._links.self.href} user = {user}/>);
		});
		
		return(
			<table>
				<tbody>
					<tr>
						<th>Username</th>
						<th>password</th>
						<th>firstname</th>
						<th>lastname</th>
						<th>email</th>
						<th>phone</th>
					</tr>
					{users}
				</tbody>
			</table>
		)
	}
}
class User extends React.Component{
	render(){
		//console.log("not reach");
		return(
			<tr>
				<td>{this.props.user.username}</td>
				<td>{this.props.user.password}</td>
				<td>{this.props.user.firstname}</td>
				<td>{this.props.user.lastname}</td>
				<td>{this.props.user.email}</td>
				<td>{this.props.user.phone}</td>
			</tr>
		)
	}	
}

ReactDOM.render(
	<App/>,
	document.getElementById('react')
)























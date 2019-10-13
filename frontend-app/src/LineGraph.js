import React from 'react';
import * as Recharts from 'recharts';
import createReactClass from 'create-react-class';

const {LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, Label} = Recharts;

const DataLineChart = createReactClass({
	render () {
        if (this.props.error) {
            return <div>MultiSelect: {this.props.error.message}</div>;
        } else if (!this.props.isLoaded) {
            return <div>Loading {this.props.name} MultiSelect...</div>;
        } else {
            return (
                <LineChart width={800} height={400} data={this.props.data}
                    margin={{top: 5, right: 30, left: 20, bottom: 5}}>
                <XAxis dataKey="day" angle={-45} textAnchor="end" height={100}/>
                <YAxis yAxisId="clicks" orientation="left" dataKey="clicks" width={150} >
                    <Label value="Clicks" position="middle" angle={-90} />
                </YAxis>
                <YAxis yAxisId="impressions" orientation="right" dataKey="impressions" width={175} >
                    <Label value="Impressions" position="middle" angle={90} />
                </YAxis>
                <CartesianGrid strokeDasharray="3 3"/>
                <Tooltip/>
                <Legend />
                <Line yAxisId="clicks" type="monotone" dataKey="clicks" stroke="#8884d8" activeDot={{r: 8}}/>
                <Line yAxisId="impressions" type="monotone" dataKey="impressions" stroke="#82ca9d" />
                </LineChart>
            );
        }
    }
});

export default DataLineChart;

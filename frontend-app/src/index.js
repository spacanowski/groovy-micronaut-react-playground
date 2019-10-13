import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import DataLineChart from './LineGraph';
import MultiSelect from './MultiSelect'
import * as serviceWorker from './serviceWorker';
import axios from 'axios';
import createReactClass from 'create-react-class';

let Parent = createReactClass({
    getInitialState() {  	
        return {isLoaded: false,
                error: null,
                datasources:[],
                campains:[],
                chartData: [],
                datasourceSelectedOptions: [],
                campainSelectedOptions: [],
                selectLabel: 'All Datasources and all campains'};
    },
    fetchData(address, setData) {
        fetch(address)
        .then(res => res.json())
        .then(
            setData,
            (error) => {
                this.setState({
                    isLoaded: true,
                    error
                });
            }
        )
    },
    componentDidMount() {
        this.fetchData('http://localhost:8080/data/datasources', (result) => {
            this.setState({
                isLoaded: true,
                datasources: result.map(i => {return {value: i, label: i}})
            });
        });
        this.fetchData('http://localhost:8080/data', (result) => {
            this.setState({
                isLoaded: true,
                chartData: result
            });
        });
    },
    async datasourceSelectHandler (selected) {
        let params = new URLSearchParams();
        selected.forEach(({value}) => params.append('datasources', value))
        let result = await axios.get('http://localhost:8080/data/campains', { params: params });
        this.setState({
            campainSelectedOptions: [],
            campains: result.data.map(i => {return {value: i, label: i}}),
            datasourceSelectedOptions: selected
        });
    },
    campainSelectHandler (selected) {
        this.setState({
            campainSelectedOptions: selected
        });
    },
    async applyFilters() {
        let params = new URLSearchParams();
        this.state.datasourceSelectedOptions.forEach(({value}) => params.append('datasources', value))
        this.state.campainSelectedOptions.forEach(({value}) => params.append('campains', value))
        let result = await axios.get('http://localhost:8080/data', { params: params });

        let campainsLabel = this.state.campainSelectedOptions.length < 1 ?
                                'All Campains' :
                                this.state.campainSelectedOptions.length > 5 ?
                                    'Multiple campains' :
                                    'Campains ' + this.state.campainSelectedOptions.map(({value}) => `"${value}"`).join(' and ')

        this.setState({
            chartData: result.data,
            selectLabel: 'Datasource ' + this.state.datasourceSelectedOptions.map(({value}) => `"${value}"`).join(' and ') + '; ' + campainsLabel
        });
    },
    render() {
        return (
            <div>
                <div id="selects">
                    <MultiSelect name= 'datasource-select'
                                    selectLabel = 'Datasources'
                                    data={this.state.datasources}
                                    isLoaded={this.state.isLoaded}
                                    error={this.state.error}
                                    changeHandler={this.datasourceSelectHandler}
                                    selectedOptions={this.state.datasourceSelectedOptions}
                                    noItemsMsg='Error getting data'/>
                    <MultiSelect name= 'campains-selct'
                                    selectLabel = 'Campains'
                                    data={this.state.campains}
                                    isLoaded={this.state.isLoaded}
                                    error={this.state.error}
                                    changeHandler={this.campainSelectHandler}
                                    selectedOptions={this.state.campainSelectedOptions}
                                    noItemsMsg='First select datasource'/>
                    <div id="button">
                        <button onClick={this.applyFilters}>
                        Apply
                        </button>
                    </div>
                </div>
                <div id="chart">
                    <div id="label">
                        <span>{this.state.selectLabel}</span>
                    </div>
                    <DataLineChart data={this.state.chartData} isLoaded={this.state.isLoaded} error={this.state.error}/>
                </div>
            </div>
        )
    }
});

ReactDOM.render(
    <Parent />,
    document.getElementById('container')
);

serviceWorker.unregister();

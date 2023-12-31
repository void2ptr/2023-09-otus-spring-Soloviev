const path                          = require('path')
//const HTMLWebpackPlugin             = require('html-webpack-plugin');
//const {CleanWebpackPlugin}          = require('clean-webpack-plugin')
//const MiniCssExtractPlugin          = require('mini-css-extract-plugin');
//const CssMinimizerPlugin            = require('css-minimizer-webpack-plugin');
//const TerserWebpackPlugin           = require('terser-webpack-plugin');
const ESLintPlugin                  = require('eslint-webpack-plugin');
//const TerserPlugin                  = require('terser-webpack-plugin');

const PACKAGE = require('./package.json');

/**
 * JS loaders
 * @returns
 */
const jsLoaders = () => {
//    const loaders = [{
//        loader: 'babel-loader',
//        options: babelOptions()
//    }];

    if (isDev) {
        // loaders.push('eslint-webpack-plugin') // deprecated !
        loaders.push('eslint-loader');
    }

    return loaders;
};


/*******************************
 * main
 */
module.exports = {
    context: path.resolve(__dirname, 'src'), // dir root
    mode: 'development',
    stats: 'errors-warnings',
    entry: {
        main: ['regenerator-runtime/runtime', './index.js' ]
    },
    output: {
        path          : path.resolve(__dirname, 'dist/books')
//        filename      : filename('.js'),
//        library       : filename(''),
//        libraryTarget : 'umd',
//        umdNamedDefine: true
    },
    resolve: {
        extensions: ['.js', '.json', '.ts'],
    },
    plugins: [
        new ESLintPlugin({
            fix: true
        })
    ],
    module: {
        rules: [
//            { test: /\.less$/,    use: scssLoader('less-loader') },
//            { test: /\.css$/,     use: scssLoader()              },
//            { test: /\.s[ac]ss$/, use: scssLoader('sass-loader') },
//            { test: /\.js$/, exclude: /node_modules/, use: jsLoaders() },
            { test: /\.ts$/, exclude: /node_modules/, use: jsLoaders() }
        ]
    }

};
